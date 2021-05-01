<#import "parts/common.ftl" as c>
<#import "parts/register.ftl" as r>

<@c.page>
    <@r.page path="patientRegister">
        <div class="form-group row">
            <label for="email" class="col-md-4 col-form-label text-md-right">Email</label>
            <div class="col-md-6">
                <input type="email" id="email" class="form-control" name="email">
            </div>
        </div>
        <div class="form-group row">
            <label for="address" class="col-md-4 col-form-label text-md-right">Адрес</label>
            <div class="col-md-6">
                <input type="text" id="address" class="form-control" name="address">
            </div>
        </div>

        <div class="form-group row">
            <label for="phone" class="col-md-4 col-form-label text-md-right">Телефон</label>
            <div class="col-md-6">
                <input type="text" id="phone" class="form-control" name="phone" placeholder="+7-999-999-99-99">
            </div>
        </div>
    </@r.page>
</@c.page>