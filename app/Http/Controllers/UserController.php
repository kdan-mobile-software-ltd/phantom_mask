<?php

namespace App\Http\Controllers;

use App\Models\PurchaseHistories;
use App\Models\Users;
use App\Validators\UserValidator;
use Illuminate\Http\Request;

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

        $startAt = $request->get('startAt');
        $endAt   = $request->get('endAt');

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
}
