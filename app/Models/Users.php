<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Users extends Model
{
    protected $table = 'users';

    protected $casts = [
        'name'         => 'string',
        'cash_balance' => 'float',
        'created_at'   => 'datetime:Y-m-d H:i:s',
        'updated_at'   => 'datetime:Y-m-d H:i:s',
    ];

    public function purchaseHistories()
    {
        return $this->hasMany(PurchaseHistories::class, 'user_id');
    }
}
