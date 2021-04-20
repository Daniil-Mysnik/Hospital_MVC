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
                <input type="text" id="speciality" class="form-control" name="room">
            </div>
        </div>
        <div class="form-group row">
            <label for="dateStart" class="col-md-4 col-form-label text-md-right">Дата начала действия расписания</label>
            <div class="col-md-6">
                <input type="date" name="dateStart" id="dateStart" value="${.now?date}">
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
            <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".multi-collapse"
                   aria-expanded="true" aria-controls="multiCollapseExample1 multiCollapseExample2">Расписание по
            дням</input>

            <div class="collapse multi-collapse show" id="multiCollapseExample1">
                <div class="form-group row">
                    <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего дня</label>
                    <div class="col-md-6">
                        <input type="time" name="timeStart" id="timeStart">
                    </div>
                    <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего дня</label>
                    <div class="col-md-6">
                        <input type="time" name="timeEnd" id="timeEnd">
                    </div>
                </div>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox1" value="Mon"> Понедельник
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox2" value="Tue"> Вторник
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="Wed"> Среда
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="Thu"> Четверг
                </label>
                <label class="form-check-inline">
                    <input class="form-check-input" type="checkbox" id="inlineCheckbox3" value="Fri"> Пятница
                </label>
            </div>
            <div class="collapse multi-collapse" id="multiCollapseExample2">

                <div>
                    <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".monday"
                           aria-expanded="true" aria-controls="monday">Понедельник</input>
                    <div class="collapse monday" id="monday">
                        <div class="form-group row">
                            <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStart" id="timeStart">
                            </div>
                            <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEnd" id="timeEnd">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".tuesday"
                           aria-expanded="true" aria-controls="tuesday">Вторник</input>
                    <div class="collapse tuesday" id="tuesday">
                        <div class="form-group row">
                            <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStart" id="timeStart">
                            </div>
                            <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEnd" id="timeEnd">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".wednesday"
                           aria-expanded="true" aria-controls="wednesday">Среда</input>
                    <div class="collapse wednesday" id="wednesday">
                        <div class="form-group row">
                            <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStart" id="timeStart">
                            </div>
                            <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEnd" id="timeEnd">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".thursday"
                           aria-expanded="true" aria-controls="thursday">Четверг</input>
                    <div class="collapse thursday" id="thursday">
                        <div class="form-group row">
                            <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStart" id="timeStart">
                            </div>
                            <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEnd" id="timeEnd">
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <input class="form-check-input" type="checkbox" data-toggle="collapse" data-target=".friday"
                           aria-expanded="true" aria-controls="friday">Пятница</input>
                    <div class="collapse friday" id="friday">
                        <div class="form-group row">
                            <label for="timeStart" class="col-md-4 col-form-label text-md-right">Начало рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeStart" id="timeStart">
                            </div>
                            <label for="timeEnd" class="col-md-4 col-form-label text-md-right">Окончание рабочего
                                дня</label>
                            <div class="col-md-6">
                                <input type="time" name="timeEnd" id="timeEnd">
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

