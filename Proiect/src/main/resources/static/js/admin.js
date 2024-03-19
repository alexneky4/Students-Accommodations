async function deleteDormitory(event) {
    const dormitoryLineGroupSelected = event.target.closest('.dormitory-line-group');
    if(event.button === 0) {
        const dormitoryName = dormitoryLineGroupSelected.children[0].innerText;
        console.log(dormitoryName);
        dormitoryLineGroupSelected.remove();

        const url = '/dormitories';

        try {
            const response = await fetch(url, {
                method: 'DELETE', 
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(dormitoryName),
            });
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            } else {
                var jsonResponse = await response.json();
                console.log(jsonResponse);
            }
        } catch (error) {
            console.error('There has been a problem with your fetch operation:', error);
        }
    }
}

function addRoom() {
    var groupRoom = document.createElement("div");
    groupRoom.classList.add("group-room");
    var inputRoomCapacity = document.createElement("input");
    inputRoomCapacity.classList.add("room-input-capacity");
    inputRoomCapacity.setAttribute("type", "number");
    inputRoomCapacity.setAttribute("placeholder", "Capacity");
    groupRoom.appendChild(inputRoomCapacity);
    var deleteRoomButton = document.createElement("button");
    deleteRoomButton.classList.add("x-button");
    deleteRoomButton.innerText = "X";
    deleteRoomButton.addEventListener('click', deleteRoom);
    groupRoom.appendChild(deleteRoomButton);
    var roomListContainer = document.getElementsByClassName("room-list-container")[0];
    roomListContainer.appendChild(groupRoom);
}

function deleteRoom(event) {
    const roomSelected = event.target.closest('.group-room');
    roomSelected.remove();
}

async function addDormitory() {
    var existingErrorMessages = document.getElementsByClassName("error-message");

    if(existingErrorMessages.length > 0) {
        existingErrorMessages[0].remove();
    }

    if(!document.getElementById("dormitory-input-name").value) {
        var errorMessage = document.createElement("p");
        errorMessage.classList.add("error-message");
        errorMessage.innerText = "Dormitory name cannot be empty";
        var dormitoryForm = document.getElementById("dormitoryForm");
        dormitoryForm.appendChild(errorMessage);
        return;
    }

    var dormitoryData = [];
    var dormitoryName = document.getElementById("dormitory-input-name").value;
    dormitoryData.push(dormitoryName);

    var roomListContainer = document.getElementsByClassName("room-list-container")[0];
    for(var i = 0; i < roomListContainer.children.length; i++) {
        var roomCapacity = roomListContainer.children[i].children[0].value;
        dormitoryData.push(roomCapacity);
        if(!roomCapacity) {
            var errorMessage = document.createElement("p");
            errorMessage.classList.add("error-message");
            errorMessage.innerText = "Room capacity cannot be empty";
            var dormitoryForm = document.getElementById("dormitoryForm");
            dormitoryForm.appendChild(errorMessage);
            return;
        }
    }

    const url = '/admin';
    try {
        const response = await fetch(url, {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(dormitoryData),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        } else {
            var jsonResponse = await response.json();
            console.log(jsonResponse);
        }
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }

    var dormitoryLineGroup = document.createElement("div");
    dormitoryLineGroup.classList.add("dormitory-line-group");
    var dormitoryName = document.createElement("li");
    dormitoryName.innerText = dormitoryData[0];
    dormitoryLineGroup.appendChild(dormitoryName);
    var xButton = document.createElement("button");
    xButton.classList.add("x-button");
    xButton.innerText = "X";
    xButton.addEventListener('click', deleteDormitory);
    dormitoryLineGroup.appendChild(xButton);
    var dormitoryList = document.getElementsByClassName("scrollable-area")[0];
    dormitoryList.appendChild(dormitoryLineGroup);


    var succesMessage = document.createElement("p");
    succesMessage.classList.add("success-message");
    succesMessage.innerText = "Dormitory added succesfully";
    var dormitoryForm = document.getElementById("dormitoryForm");
    dormitoryForm.appendChild(succesMessage);
}

async function startDormitoryPhase() {
    var time = document.getElementsByClassName("datetime")[0].value;

    if(!time) {
        var errorMessage = document.createElement("p");
        errorMessage.classList.add("error-message");
        errorMessage.innerText = "Time cannot be empty";
        var submitInfo = document.getElementsByClassName("submit-info")[0];
        submitInfo.appendChild(errorMessage);
        return;
    }

    const url = '/admin/startDormitoryPhase';
    try {
        const response = await fetch(url, {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(time),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        } else {
            var jsonResponse = await response.json();
            console.log(jsonResponse);
        }
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }

    window.location.href = "/admin";
}

async function endDormitoryPhase()
{
    var time = document.getElementsByClassName("datetime")[0].value;

    if(!time) {
        var errorMessage = document.createElement("p");
        errorMessage.classList.add("error-message");
        errorMessage.innerText = "Time cannot be empty";
        var submitInfo = document.getElementsByClassName("submit-info")[0];
        submitInfo.appendChild(errorMessage);
        return;
    }
    
    const url = '/admin/endDormitoryPhase';
    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(time),
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        } else {
            var jsonResponse = await response.json();
            console.log(jsonResponse);
        }
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }

    window.location.href = "/admin";
}

async function endStudentPhase()
{
    const url = '/admin/endStudentPhase';
    try {
        const response = await fetch(url, {
            method: 'POST',
        });
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        } else {
            var jsonResponse = await response.json();
            console.log(jsonResponse);
        }
    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
    }

    window.location.href = "/admin";
}


