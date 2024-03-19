var dormitoryLineGroups = document.querySelectorAll('.dormitory-line-group');
var maxPriority = 1;

dormitoryLineGroups.forEach(dormitoryLineGroup => {
    dormitoryLineGroup.addEventListener('click', addPriority);
});

function addPriority(event) {
    const dormitoryLineGroupSelected = event.target.closest('.dormitory-line-group');
    if(event.button === 0) {
        console.log(dormitoryLineGroupSelected);
        dormitoryLineGroupSelected.classList.toggle('selected');
        dormitoryLineGroupSelected.children[0].textContent = maxPriority;
        maxPriority++;
        dormitoryLineGroupSelected.removeEventListener('click', addPriority);
        dormitoryLineGroupSelected.addEventListener('click', removePriority);
    }
    sortDormitories();
}

function removePriority(event) {
    const dormitoryLineGroupSelected = event.target.closest('.dormitory-line-group');
    if(event.button === 0) {
        console.log(dormitoryLineGroupSelected);
        dormitoryLineGroupSelected.classList.toggle('selected');
        dormitoryLineGroups.forEach(dormitoryLineGroup => {
            if(dormitoryLineGroup.children[0].textContent > dormitoryLineGroupSelected.children[0].textContent) {
                dormitoryLineGroup.children[0].textContent--;
            }
        });
        dormitoryLineGroupSelected.children[0].textContent = 0;
        maxPriority--;
        dormitoryLineGroupSelected.removeEventListener('click', removePriority);
        dormitoryLineGroupSelected.addEventListener('click', addPriority);
        sortDormitories();
    }
}

function sortDormitories() {
    const dormitoryLineGroupsArray = Array.from(document.querySelectorAll('.dormitory-line-group'));
    const dormitoryListContainer = document.querySelector('.scrollable-area');
    dormitoryLineGroupsArray.sort((a, b) => {
        if(a.children[0].textContent == 0) {
            return 1;
        }
        if(b.children[0].textContent == 0) {
            return -1;
        }
        return a.children[0].textContent - b.children[0].textContent;
    }
    );
    dormitoryListContainer.innerHTML = '';
    dormitoryLineGroupsArray.forEach(dormitoryLineGroup => {
        dormitoryListContainer.appendChild(dormitoryLineGroup);
    }
    );
}

async function submit() {
    var dormitoryLines = document.getElementsByClassName("dormitory-line-group");
    var dormitoryData = [];

    for (var i = 0; i < dormitoryLines.length; i++) {
        var dormitoryPriority = dormitoryLines[i].children[0].innerText;
        console.log(dormitoryPriority);
        if(dormitoryPriority == 0) {
            continue;
        }
        dormitoryLines[i].classList.toggle('selected');
        dormitoryLines[i].children[0].innerText = 0;
        dormitoryLines[i].removeEventListener('click', removePriority);
        dormitoryLines[i].addEventListener('click', addPriority);
        maxPriority = 1;
        var dormitoryName = dormitoryLines[i].getElementsByClassName("dormitory-element")[0].innerText;

        dormitoryData.push(dormitoryName);
    }

    var url = "/dormitories";

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

    var submitButton = document.getElementById("submitButton");
    submitButton.innerText = "Submitted";
    setTimeout(function() {
        submitButton.innerText = "Submit";
    }, 5000);

    var dormitoryArea = document.getElementById("dormitoryArea");
    var successTest = document.createElement("p");
    successTest.innerText = "Dormitories submitted successfully!";
    successTest.style.color = "green";
    dormitoryArea.appendChild(successTest);
    setTimeout(function() {
        dormitoryArea.removeChild(successTest);
    }, 5000);
}
