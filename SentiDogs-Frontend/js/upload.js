

FilePond.registerPlugin(FilePondPluginMediaPreview);
// Get a reference to the file input element
const inputElement = document.querySelector('input[type="file"]');




const pond = FilePond.create(inputElement);



function travelhelper(){
    //document.getElementById("element").innerHTML ="Type you want to upload: Travel Destination";
    document.getElementById("dropdownMenuButton1").innerText = "Travel Destination";

}

function resthelper(){
    //document.getElementById("element").innerHTML ="Type you want to upload: Restaurant";
    document.getElementById("dropdownMenuButton1").innerText = "Restaurant";
}

function submittrans(){
    $(function() {
        //const url = "http://54.145.128.111:8080/file/" + temp;
        const url = " http://54.145.128.111:8080/analyze";

        // axios.post(url, {
        //
        // })
        //     .then(res => {
        //         const out = res.data.response;
        //         console.log(out);
        //
        //         });
        //     });


})


}