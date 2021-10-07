<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Pharmacies extends Model
{
    protected $table = 'pharmacies';

    protected $casts = [
        'created_at' => 'datetime:Y-m-d H:i:s',
        'updated_at' => 'datetime:Y-m-d H:i:s',
    ];

    public function openingHours()
    {
        return $this->hasMany(OpeningHours::class, 'pharmacy_id');
    }

    public function products()
    {
        return $this->hasMany(Products::class, 'pharmacy_id');
    }
}
