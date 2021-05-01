<#import "parts/common.ftl" as c>
<#import "parts/register.ftl" as r>

<@c.page>
    <@r.page path="adminRegister">
        <div class="form-group row">
            <label for="position" class="col-md-4 col-form-label text-md-right">Должность</label>
            <div class="col-md-6">
                <input type="text" id="position" class="form-control" name="position">
            </div>
        </div>
    </@r.page>
</@c.page>