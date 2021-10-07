<?php

namespace App\Http\Controllers;

use App\Models\Pharmacies;
use App\Validators\PharmacyValidator;
use Carbon\Carbon;
use Illuminate\Http\Request;

class PharmacyController extends Controller
{
    /**
     * List all pharmacies that are open at a certain time, and on a day of the week if requested
     *
     * @return json
     */
    public function getPharmaciesByPeriod(Request $request)
    {
        PharmacyValidator::checkByPeriod($request->all());

        $date = $request->get('date') ?? 0;
        $time = $request->get('time');

        // 轉換日期為星期
        if ($date) {
            $date = Carbon::parse($date)->toDayDateTimeString();
            $date = substr($date, 0, 3);
        }

        $query = Pharmacies::select([
            'id',
            'name',
        ])
            ->whereIn('id', function ($sub) use ($time, $date) {
                $sub = $sub->select('pharmacy_id')
                    ->from('opening_hours');

                if ($date) {
                    $sub->where('week_day', $date);
                }

                return $sub->where('start_at', '<=', $time)
                    ->where('end_at', '>=', $time);
            })
            ->with(['openingHours' => function ($sub) {
                return $sub->select(['pharmacy_id', 'week_day', 'start_at', 'end_at']);
            }])
            ->get()
            ->toArray();

        return [
            'data' => $query,
        ];
    }
}
