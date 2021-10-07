<?php
namespace App\Validators;

use Validator;
use App\Exceptions\FailException;
use Exception;

class BaseValidator
{
    # 自訂義檢查規則
    protected $rules = [];

    # 自訂義錯誤說明
    protected $messages = [];

    # input data
    protected $inputData;

    # validator object
    protected $validator;

    public function __construct($data, $rules = [], $messages = [])
    {

        $this->inputData = $data ?: request()->input();

        # 取得自訂義錯誤訊息
        $this->messages = array_merge($this->messages, $messages);

        $this->rules = array_merge($this->rules, $rules);

        $this->validator = Validator::make($this->inputData, $this->rules, $this->messages);
    }

    /**
     * 檢查資料是否正確, 資料有問題時, 直接丟出 ValidatorException 的錯誤
     *
     * @return void
     */
    public function check ()
    {
        if ($this->fails()) {
            throw new Exception($this->errors());
            // throw new ValidatorException($this->errors());
        }
    }

    /**
     * 觸發規則檢查，並回傳是否有錯誤
     *
     * @return boolean
     */
    public function fails()
    {
        return $this->validator->fails();
    }

    public function validator ()
    {
        return $this->validator;
    }

    /**
     * 取得錯誤的資料內容
     * 回傳內容 [ string{欄位} => [string{錯誤說明} ... ]]
     * @return array
     */
    public function errors ()
    {
        return $this->validator->errors();
    }

}
