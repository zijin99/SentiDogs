

FilePond.registerPlugin(FilePondPluginMediaPreview);
// Get a reference to the file input element
const inputElement = document.querySelector('input[type="file"]');

const pond = FilePond.create(inputElement);
let temp;


function travelhelper(){
    //document.getElementById("element").innerHTML ="Type you want to upload: Travel Destination";
    document.getElementById("dropdownMenuButton1").innerText = "Travel Destination";
    temp = "travel";
}

function resthelper(){
    //document.getElementById("element").innerHTML ="Type you want to upload: Restaurant";
    document.getElementById("dropdownMenuButton1").innerText = "Restaurant";
    temp = "restaurant";
}


function submittrans(input){

    $(function() {
        const url = "http://54.145.128.111:8080/analyze";
        const fileType = temp
        const texta = document.getElementById("textArea").value;
        axios.post(url, {
            transcription:texta, fileType:fileType
        })
            .then(res => {
                $.each(res.data, function(i, f) {
                    const tblRow = "<tr>" + "<td>" + f.name + "</td>" +
                        "<td>" + f.sentiment + "</td>" + "<td>" + "<a href=" + f.metadataMap.wikipedia_url+ ">"
                        + f.name +"</a>" +  "</td>" + "<td>" + "<a href=" + f.metadataMap.local_map+ ">"+
                        f.name +"</a>" + "</td>" + "</tr>"
                    $(tblRow).appendTo("#userdata tbody");
                });
            });
    });
}