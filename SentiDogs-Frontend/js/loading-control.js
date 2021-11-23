/**
 * Created by hrwhisper on 2016/4/25.
 */
var loading_control = {
    opts: {
        lines: 14, // The number of lines to draw
        length: 0, // The length of each line
        width: 17, // The line thickness
        radius: 19, // The radius of the inner circle
        scale: 1.1, // Scales overall size of the spinner
        corners: 1, // Corner roundness (0..1)
        speed: 1.3, // Rounds per second
        rotate: 0, // The rotation offset
        animation: 'spinner-line-fade-quick', // The CSS animation name for the lines
        direction: 1, // 1: clockwise, -1: counterclockwise
        color: '#d2d0d0', // CSS color or array of colors
        fadeColor: 'transparent', // CSS color or array of colors
        top: '50%', // Top position relative to parent
        left: '50%', // Left position relative to parent
        shadow: '0 0 1px transparent', // Box-shadow for the lines
        zIndex: 2000000000, // The z-index (defaults to 2e9)
        className: 'spinner', // The CSS class to assign to the spinner
        position: 'absolute', // Element positioning
    },
    spinner: null,
    div_wait: null,
    div_wait_bg: null,

    start: function () {
        if (!this.div_wait) {
            var div = document.createElement("div");
            div.id = "foo";
            document.body.appendChild(div);
            this.div_wait = div;
        }

        if (!this.div_wait_bg) {
            var div = document.createElement("div");
            div.id = "waiting-bg";
            div.style.cssText = "width:100%; height:100%; background-color:#000; filter:alpha(opacity=60);-moz-opacity:0.6; opacity:0.6; position:fixed; left:0px; top:0px; display:none;  z-index:1000;";

            document.body.appendChild(div);
            this.div_wait_bg = div;
        }

        if (!this.spinner) {
            this.spinner = new Spinner(this.opts);
        }

        this.div_wait_bg.style.display = "block";
        this.spinner.spin(this.div_wait);
    },

    stop: function () {
        if(this.spinner)
            this.spinner.stop();
        this.div_wait_bg.style.display = "none";
    }
};

