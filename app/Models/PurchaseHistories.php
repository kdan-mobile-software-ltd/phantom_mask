<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class PurchaseHistories extends Model
{
    const UPDATED_AT = null;

    protected $table = 'purchase_histories';

    protected $casts = [
        'purchase_number'    => 'integer',
        'transaction_amount' => 'float',
        'transaction_date'   => 'datetime:Y-m-d H:i:s',
        'created_at'         => 'datetime:Y-m-d H:i:s',
    ];
}
