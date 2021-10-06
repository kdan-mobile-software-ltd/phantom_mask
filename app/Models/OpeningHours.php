<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class OpeningHours extends Model
{
    const MON = 'Mon';
    const TUE = 'Tue';
    const WED = 'Wed';
    const THU = 'Thu';
    const FRI = 'Fri';
    const SAT = 'Sat';
    const SUN = 'Sun';

    protected $table = 'opening_hours';

    protected $casts = [
        'start_at'   => 'datetime:H:i',
        'end_at'     => 'datetime:H:i',
        'created_at' => 'datetime:Y-m-d H:i:s',
        'updated_at' => 'datetime:Y-m-d H:i:s',
    ];
}
