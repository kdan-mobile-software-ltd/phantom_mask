<?php

namespace App\Validators;

use App\Validators\BaseValidator;

class PharmacyValidator extends BaseValidator
{
    public static function checkByPeriod($data)
    {
        (new static($data, [
            'time' => [
                'required',
                'date_format:H:i:s',
            ],
            'date' => [
                'nullable',
                'date_format:Y/m/d',
            ],
        ]))->check();
    }

    public static function checkByPharmacy($data)
    {
        (new static($data, [
            'pharmacyId' => [
                'required',
                'integer',
            ],
            'sorts' => [
                'nullable',
            ],
        ]))->check();
    }

    public static function checkByPrice($data)
    {
        (new static($data, [
            'min' => [
                'required',
                'numeric',
                'min:0.01',
            ],
            'max' => [
                'required',
                'numeric',
                'max:9999999',
            ],
        ]))->check();
    }
}