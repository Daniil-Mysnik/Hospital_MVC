<#macro page>
    <!doctype html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
<#--        <link type="text/css" rel="stylesheet" href="${requestcontext.contextPath}/static/bootstrap.css">-->
        <!— Latest compiled and minified CSS —>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">

        <!— jQuery library —>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>

        <!— Popper JS —>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>

        <!— Latest compiled JavaScript —>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

        <link href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.7.1/css/bootstrap-datepicker.min.css" rel="stylesheet"/>
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous"></head>

<#--        <script src="https://code.jquery.com/jquery-3.4.1.min.js" ></script>-->

        <title>hospital</title>
    </head>
    <body>
    <#include "navbar.ftl">
    <#nested>
    </body>
    </html>
</#macro>