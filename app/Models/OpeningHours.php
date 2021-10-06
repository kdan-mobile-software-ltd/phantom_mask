<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class OpeningHours extends Model
{
    const MON = 'mon';
    const TUE = 'tue';
    const WED = 'wed';
    const THU = 'thu';
    const FRI = 'fri';
    const SAT = 'sat';
    const SUN = 'sun';

    protected $table = 'opening_hours';

    protected $casts = [
        'start_at'   => 'datetime:H:i',
        'end_at'     => 'datetime:H:i',
        'created_at' => 'datetime:Y-m-d H:i:s',
        'updated_at' => 'datetime:Y-m-d H:i:s',
    ];
}
