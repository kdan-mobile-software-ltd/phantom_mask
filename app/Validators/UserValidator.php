<?php

namespace App\Validators;

use App\Validators\BaseValidator;

class UserValidator extends BaseValidator
{
    public static function checkByPeriod($data)
    {
        (new static($data, [
            'startAt' => [
                'required',
                'date_format:Y/m/d',
            ],
            'endAt' => [
                'required',
                'date_format:Y/m/d',
            ],
        ]))->check();
    }

    public static function checkPurchaseMasks($data)
    {
        (new static($data, [
            'userId' => [
                'required',
                'integer',
            ],
            'productId' => [
                'required',
                'integer',
            ],
        ]))->check();
    }
}