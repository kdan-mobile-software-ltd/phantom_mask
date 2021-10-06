<?php

namespace App\Console\Commands;

use App\Models\Masks;
use App\Models\OpeningHours;
use App\Models\Pharmacies;
use App\Models\Products;
use App\Models\PurchaseHistories;
use App\Models\Users;
use Exception;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;

class SeedByJson extends Command
{
    /**
     * The name and signature of the console command.
     *
     * @var string
     */
    protected $signature = 'seed:json';

    /**
     * The console command description.
     *
     * @var string
     */
    protected $description = 'Command description';

    /**
     * Create a new command instance.
     *
     * @return void
     */
    public function __construct()
    {
        parent::__construct();

        $this->weekSort = [
            0 => OpeningHours::MON,
            1 => OpeningHours::TUE,
            2 => OpeningHours::WED,
            3 => OpeningHours::THU,
            4 => OpeningHours::FRI,
            5 => OpeningHours::SAT,
            6 => OpeningHours::SUN,
        ];
    }

    /**
     * Execute the console command.
     *
     * @return mixed
     */
    public function handle()
    {
        try {
            $path       = storage_path() . "/data/pharmacies.json";
            $pharmacies = json_decode(file_get_contents($path), true);

            $path  = storage_path() . "/data/users.json";
            $users = json_decode(file_get_contents($path), true);

            DB::beginTransaction();
            // 先建立藥局資料
            foreach ($pharmacies as $pharmacy) {
                $this->buildPharmacy($pharmacy);
            }

            // 登記消費者資料
            foreach ($users as $user) {
                $this->buildUser($user);
            }

            DB::commit();
        } catch (\Throwable $th) {
            DB::rollback();
            $this->error('MSG: ' . $th->getMessage());
            $this->error('LINE: ' . $th->getLine());
            //throw $th;
        }
    }

    private function buildPharmacy($data)
    {
        $pharmacy               = new Pharmacies();
        $pharmacy->name         = $data['name'];
        $pharmacy->cash_balance = $data['cashBalance'];
        $pharmacy->save();

        // 登記各藥局的營業時間
        $openingHours = explode('/', $data['openingHours']);

        foreach ($openingHours as $hours) {
            // 切割字串來比對格式
            $hours = trim($hours);
            preg_match('/(.+)\s(\d{2}\:\d{2})(\s\-\s)(\d{2}\:\d{2})/', $hours, $splitDateTime);
            $weekDay = $splitDateTime[1];

            if (str_contains($weekDay, ',')) {
                // 指定星期的格式
                $days = explode(',', $weekDay);

                foreach ($days as $day) {
                    $openingHour              = new OpeningHours();
                    $openingHour->pharmacy_id = $pharmacy->id;
                    $openingHour->week_day    = trim($day);
                    $openingHour->start_at    = $splitDateTime[2];
                    $openingHour->end_at      = $splitDateTime[4];
                    $openingHour->save();
                }
            } elseif (str_contains($weekDay, '-')) {
                // 時間區間的格式
                list($start, $end) = explode(' - ', $weekDay);
                $save              = false;

                foreach ($this->weekSort as $day) {
                    if ($day == $start) {
                        $save = true;
                    }

                    if ($save) {
                        $openingHour              = new OpeningHours();
                        $openingHour->pharmacy_id = $pharmacy->id;
                        $openingHour->week_day    = $day;
                        $openingHour->start_at    = $splitDateTime[2];
                        $openingHour->end_at      = $splitDateTime[4];
                        $openingHour->save();
                    }

                    if ($end == $start) {
                        $save = false;
                    }
                }
            } else {
                $openingHour              = new OpeningHours();
                $openingHour->pharmacy_id = $pharmacy->id;
                $openingHour->week_day    = trim($weekDay);
                $openingHour->start_at    = $splitDateTime[2];
                $openingHour->end_at      = $splitDateTime[4];
                $openingHour->save();
            }
        }

        // 登記各藥局販售的口罩
        $products = $data['masks'];

        foreach ($products as $productInfo) {
            preg_match('/(.+)\s\((.+)\)\s\((\d{1,3})\s.+\)/', $productInfo['name'], $splitProduct);
            $mask = $this->findMask($splitProduct[1], $splitProduct[2]);

            $product              = new Products();
            $product->pharmacy_id = $pharmacy->id;
            $product->mask_id     = $mask->id;
            $product->unit        = $splitProduct[3];
            $product->price       = $productInfo['price'];
            $product->save();
        }
    }

    /**
     * 回傳口罩
     *
     * @param string $name
     * @param string $color
     * @return \App\Models\Masks
     */
    private function findMask($name, $color)
    {
        $name  = trim($name);
        $color = trim($color);
        $mask  = Masks::where('name', $name)
            ->where('color', $color)
            ->first();

        if ($mask == null) {
            $mask        = new Masks();
            $mask->name  = $name;
            $mask->color = $color;
            $mask->save();
        }

        return $mask;
    }

    private function buildUser($data)
    {
        $user = Users::where('name', $data['name'])->first();

        if ($user == null) {
            $user               = new Users();
            $user->name         = $data['name'];
            $user->cash_balance = $data['cashBalance'];
            $user->save();
        }

        // 登記消費紀錄
        $purchaseHistories = $data['purchaseHistories'];

        foreach ($purchaseHistories as $purchaseHistory) {
            $pharmacy = Pharmacies::where('name', $purchaseHistory['pharmacyName'])
                ->first();

            if ($pharmacy == null) {
                throw new Exception('null pharmacy info');
            }

            preg_match('/(.+)\s\((.+)\)\s\((\d{1,3})\s.+\)/', $purchaseHistory['maskName'], $splitProduct);
            $product = Products::where('pharmacy_id', $pharmacy->id)
                ->where('unit', $splitProduct[3])
                ->byMask($splitProduct[1], $splitProduct[2])
                ->first();

            if ($product == null) {
                throw new Exception('null product info');
            }

            $record                     = new PurchaseHistories();
            $record->user_id            = $user->id;
            $record->pharmacy_id        = $pharmacy->id;
            $record->product_id         = $product->id;
            $record->purchase_number    = 1; // 預設購買組數都為一組
            $record->transaction_amount = $purchaseHistory['transactionAmount'];
            $record->transaction_date   = $purchaseHistory['transactionDate'];
            $record->save();
        }
    }
}
