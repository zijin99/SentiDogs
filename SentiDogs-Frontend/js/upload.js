

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