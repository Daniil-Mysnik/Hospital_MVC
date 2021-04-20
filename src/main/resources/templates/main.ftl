<#import "parts/common.ftl" as c>

<@c.page>
    <div id="slider" class="carousel slide" data-bs-ride="carousel">
        <ol class="carousel-indicators">
            <li data-target="#slider" data-slide-to="0" class="active"></li>
            <li data-target="#slider" data-slide-to="1"></li>
            <li data-target="#slider" data-slide-to="2"></li>
        </ol>
        <div class="carousel-inner">
            <div class="carousel-item active">
                <img src="static/images/carousel/1.jpg" class="d-block w-100" alt="">
            </div>
            <div class="carousel-item">
                <img src="static/images/carousel/2.jpg" class="d-block w-100" alt="">
            </div>
            <div class="carousel-item">
                <img src="static/images/carousel/3.jpg" class="d-block w-100" alt="">
            </div>
        </div>
        <a href="#slider" class="carousel-control-prev" role="button" data-slide="prev">
            <span class="carousel-control-prev-icon" aria-hidden="true"></span>
            <span class="sr-only">Previous</span>
        </a>
        <a href="#slider" class="carousel-control-next" role="button" data-slide="next">
            <span class="carousel-control-next-icon" aria-hidden="true"></span>
            <span class="sr-only">Next</span>
        </a>
    </div>
</@c.page>