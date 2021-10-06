<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Products extends Model
{
    protected $table = 'products';

    protected $casts = [
        'unit'       => 'integer',
        'price'      => 'float',
        'created_at' => 'datetime:Y-m-d H:i:s',
        'updated_at' => 'datetime:Y-m-d H:i:s',
    ];

    public function pharmacy()
    {
        return $this->belongsTo(Pharmacies::class, 'pharmacy_id');
    }

    public function mask()
    {
        return $this->belongsTo(Masks::class, 'mask_id');
    }

    public function scopeByMask($query, $name, $color)
    {
        return $query->where('mask_id', function ($sub) use ($name, $color) {
            return $sub->select('id')
                ->from('masks')
                ->where('name', $name)
                ->where('color', $color);
        });
    }
}
