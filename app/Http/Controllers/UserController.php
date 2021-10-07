<?php

namespace App\Http\Controllers;

use App\Models\Pharmacies;
use App\Models\Products;
use App\Models\PurchaseHistories;
use App\Models\Users;
use App\Validators\UserValidator;
use Carbon\Carbon;
use Exception;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class UserController extends Controller
{
    /**
     * The top x users by total transaction amount of masks within a date range
     *
     * @return json
     */
    public function getUsersByPeriod(Request $request)
    {
        UserValidator::checkByPeriod($request->all());

        $startAt = $request->get('startAt') . ' 00:00:00';
        $endAt   = $request->get('endAt') . ' 23:59:59';

        $query = PurchaseHistories::selectRaw("
                purchase_histories.user_id,
                users.name,
                SUM(purchase_histories.transaction_amount) AS total
                ")
            ->join('users', 'users.id', '=', 'purchase_histories.user_id')
            ->whereBetween('transaction_date', [$startAt, $endAt])
            ->orderByRaw("SUM(purchase_histories.transaction_amount) DESC")
            ->groupBy('purchase_histories.user_id')
            ->get()
            ->toArray();

        return [
            'data' => $query,
        ];
    }

    /**
     * The total amount of masks and dollar value of transactions that happened within a date range
     *
     * @return json
     */
    public function getTransactionsByPeriod(Request $request)
    {
        UserValidator::checkByPeriod($request->all());

        $startAt = $request->get('startAt') . ' 00:00:00';
        $endAt   = $request->get('endAt') . ' 23:59:59';

        $query = PurchaseHistories::selectRaw("SUM(transaction_amount) AS `sum`, masks.name, masks.color")
            ->join('products', 'products.id', '=', 'purchase_histories.product_id')
            ->join('masks', 'masks.id', '=', 'products.mask_id')
            ->whereBetween('transaction_date', [$startAt, $endAt])
            ->groupBy('masks.id')
            ->get()
            ->toArray();

        return [
            'data' => $query,
        ];
    }

    /**
     * Process a user purchases a mask from a pharmacy, and handle all relevant data changes in an atomic transaction
     *
     * @return void
     */
    public function purchaseMasks(Request $request)
    {
        UserValidator::checkPurchaseMasks($request->all());

        $userId    = $request->get('userId');
        $productId = $request->get('productId');

        try {
            $user     = Users::find($userId);
            $product  = Products::find($productId);
            $pharmacy = Pharmacies::find($product->pharmacy_id);

            DB::beginTransaction();
            // 消費紀錄
            $record                     = new PurchaseHistories();
            $record->user_id            = $userId;
            $record->transaction_amount = $product->price;
            $record->transaction_date   = Carbon::now()->toDateTimeString();
            $record->pharmacy_id        = $product->pharmacy_id;
            $record->product_id         = $productId;
            $record->save();

            // 收支平衡
            $user->decrement('cash_balance', $product->price);
            $pharmacy->increment('cash_balance', $product->price);
            DB::commit();
        } catch (Exception $th) {
            DB::rollback();
            throw new Exception('purchase mask(s) failed');
        }
    }

    /**
     * get all user id and name
     *
     * @return json
     */
    public function getUsers()
    {
        $query = Users::select(['id', 'name'])
            ->get()
            ->toArray();

        return [
            'data' => $query,
        ];
    }
}
