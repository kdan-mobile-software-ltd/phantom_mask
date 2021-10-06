<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Masks extends Model
{
    const UPDATED_AT = null;

    protected $table = 'masks';

    protected $casts = [
        'created_at' => 'datetime:Y-m-d H:i:s',
    ];
}
