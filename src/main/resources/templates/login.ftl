<#import "parts/common.ftl" as c>

<@c.page>
<#--    <div class="d-flex flex-column justify-content-center" style="height: 70vh">-->
<#--        <div class="container d-flex justify-content-center">-->
<#--            <form class="form-signin text-center" method="post" action="${requestcontext.contextPath}/login">-->
<#--                <h1 class="h3 mb-3 font-weight-normal">Sign in</h1>-->
<#--                <label for="inputEmail" class="sr-only">Email address</label>-->
<#--                <input name="login" type="text" class="form-control" placeholder="Логин" required autofocus>-->
<#--                <label for="inputPassword" class="sr-only">Password</label>-->
<#--                <input name="password" type="password" class="form-control" placeholder="Пароль" required>-->
<#--                <button class="btn btn-lg btn-primary btn-block" type="submit">Войти</button>-->
<#--            </form>-->
<#--        </div>-->
<#--    </div>-->

    <div class="cotainer">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <div class="card">
                    <div class="card-header">Sign In</div>
                    <div class="card-body">
                        <form class="form-signin text-center" method="post" action="${requestcontext.contextPath}/login">
                            <div class="form-group row mt-5">
                                <label for="login" class="col-md-4 col-form-label text-md-right">Логин</label>
                                <div class="col-md-6">
                                    <input type="text" id="login" class="form-control" name="login">
                                </div>
                            </div>

                            <div class="form-group row">
                                <label for="password" class="col-md-4 col-form-label text-md-right">Пароль</label>
                                <div class="col-md-6">
                                    <input type="password" id="password" class="form-control" name="password">
                                </div>
                            </div>

                            <div class="col-md-6 offset-md-4">
                                <button type="submit" class="btn btn-primary">
                                    Войти
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@c.page>
