<#import "parts/common.ftl" as c>
<#import "parts/register.ftl" as r>

<@c.page>
    <@r.page path="registerDoctor">
        <div class="form-group row">
            <label for="speciality" class="col-md-4 col-form-label text-md-right">Специальность</label>
            <div class="col-md-6">
                <input type="text" id="speciality" class="form-control" name="speciality">
            </div>
        </div>
        <div class="form-group row">
            <label for="room" class="col-md-4 col-form-label text-md-right">Кабинет</label>
            <div class="col-md-6">
                <input type="text" id="room" class="form-control" name="room">
            </div>
        </div>
        <div class="form-group row">
            <label for="dateStart" class="col-md-4 col-form-label text-md-right">Дата начала действия расписания</label>
            <div class="col-md-6">
                <input type="date" name="dateStart" id="dateStart">
            </div>
        </div>
        <div class="form-group row">
            <label for="dateEnd" class="col-md-4 col-form-label text-md-right">Дата окончания действия
                расписания</label>
            <div class="col-md-6">
                <input type="date" name="dateEnd" id="dateEnd">
            </div>
        </div>

        <div class="form-group px-5 mx-5 pb-4">
            <input class="form-check-input" type="checkbox" name="isWeekDaysSchedule" id="isWeekDaysSchedule" value="isWeekDaysSchedule" data-toggle="collapse" data-target=".multi-collapse"
                   aria-expanded="true" aria-controls="multiCollapseExample1 multiCollapseExample2">Расписание по
            дням</input>

            <div class="collapse multi-collapse show" id="multiCollapseExample1">
                <div class="form-group row">
                    <label for="timeStartEveryDay" class="col-md-4 col-form-label text-md-right">Начало рабочего дня</label>
                    <div class="col-md-6">
                        <input type="time" name="timeStartEveryDay" id="timeStartEveryDay">
                    </div>
                    <label for="timeEndEveryDay" class="col-md-4 col-form-label text-md-right">Окончание рабочего дня</label>
                    <div class="col-md-6">
                        <input type="time" name="timeEndEveryDay" id="timeEndEveryDay">
                    </div>
                </div>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="monED" id="monED" value="Mon"> Понедельник
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="tueED" id="tueED" value="Tue"> Вторник
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="wedED" id="wedED" value="Wed"> Среда
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="thuED" id="thuED" value="Thu"> Четверг
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" name="friED" id="friED" value="Fri"> Пятница
                </label>
            </div>
            <div class="collapse multi-collapse" id="multiCollapseExample2">
                <div>
                    <input class="form-check-input" type="checkbox" name="monday" id="monday" value="Mon" data-toggle="collapse" data-target=".monday"
                           aria-expanded="true" aria-controls="monday">Понедельник</input>
                    <div class="collapse monday" id="monday">
                        <div class="form-group row">
                            <label for="timeStartMon" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStartMon" id="timeStartMon">
                            </div>
                            <label for="timeEndMon" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEndMon" id="timeEndMon">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" name="tuesday" id="tuesday" value="Tue" data-toggle="collapse" data-target=".tuesday"
                           aria-expanded="true" aria-controls="tuesday">Вторник</input>
                    <div class="collapse tuesday" id="tuesday">
                        <div class="form-group row">
                            <label for="timeStartTue" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStartTue" id="timeStartTue">
                            </div>
                            <label for="timeEndTue" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEndTue" id="timeEndTue">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" name="wednesday" id="wednesday" value="Wed" data-toggle="collapse" data-target=".wednesday"
                           aria-expanded="true" aria-controls="wednesday">Среда</input>
                    <div class="collapse wednesday" id="wednesday">
                        <div class="form-group row">
                            <label for="timeStartWed" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStartWed" id="timeStartWed">
                            </div>
                            <label for="timeEndWed" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEndWed" id="timeEndWed">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" name="thursday" id="thursday" value="Thu" data-toggle="collapse" data-target=".thursday"
                           aria-expanded="true" aria-controls="thursday">Четверг</input>
                    <div class="collapse thursday" id="thursday">
                        <div class="form-group row">
                            <label for="timeStartThu" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStartThu" id="timeStartThu">
                            </div>
                            <label for="timeEndThu" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEndThu" id="timeEndThu">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" name="friday" id="friday" value="Fri" data-toggle="collapse" data-target=".friday"
                           aria-expanded="true" aria-controls="friday">Пятница</input>
                    <div class="collapse friday" id="friday">
                        <div class="form-group row">
                            <label for="timeStartFri" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStartFri" id="timeStartFri">
                            </div>
                            <label for="timeEndFri" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEndFri" id="timeEndFri">
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group row">
            <label for="duration" class="col-md-4 col-form-label text-md-right">Продолжительность приёма</label>
            <div class="col-md-6">
                <input type="number" id="duration" class="form-control" name="duration">
            </div>
        </div>
    </@r.page>
</@c.page>

