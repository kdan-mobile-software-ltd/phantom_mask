<?php

use App\Models\OpeningHours;
use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateOpeningHoursTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('opening_hours', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('pharmacy_id')->unsigned()->comment("所屬藥局 (pharmacies.id)");
            $table->enum('week_day', [
                    OpeningHours::MON,
                    OpeningHours::TUE,
                    OpeningHours::WED,
                    OpeningHours::THU,
                    OpeningHours::FRI,
                    OpeningHours::SAT,
                    OpeningHours::SUN,
                ])->comment("星期");
            $table->time('start_at')->comment("開始");
            $table->time('end_at')->comment("開始");
            $table->timestamps();

            $table->foreign('pharmacy_id')
                ->references('id')->on('pharmacies')
                ->onDelete('cascade');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('opening_hours');
    }
}
