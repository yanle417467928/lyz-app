<style type="text/css">
    body {
        margin: 0px;
        /*background: black;*/
    }

    .loading {
        width: 100px;
        height: 100px;
        margin: 50px auto;
        border: 8px solid #333;
        border-left: 8px solid #3c8dbc;
        border-radius: 100px;
        -moz-animation: 2s loadings linear infinite;
        -webkit-animation: 2s loadings linear infinite;
        -ms-animation: 2s loading linear infinite;
        animation: 2s loadings linear infinite;
    }

    @-webkit-keyframes loadings {
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

    @-moz-keyframes loadings {
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

    @-ms-keyframes loadings {
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

    @keyframes loadings {
        from {
            transform: rotate(0deg);
        }
        to {
            transform: rotate(360deg);
        }
    }

</style>
<div id="loading" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog">
        <div class="loading"></div>
    </div>
</div>