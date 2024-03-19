var students = document.querySelectorAll('.student-element');
var left = document.getElementById('students');
var right = document.getElementById('selectedStudents');


var maxPriority = 1;

students.forEach(student => {
    student.addEventListener('click', moveRight);
});

function moveRight(event) {
    const studentSelected = event.target;
    if(event.button === 0) {
        const selectedStudentGroup = document.createElement('div');
        selectedStudentGroup.classList.add('hover-effect');
        selectedStudentGroup.classList.add('selected-student-group');
        const selectedStudentPriority = document.createElement('div');
        selectedStudentPriority.classList.add('student-priority');
        selectedStudentPriority.textContent = maxPriority;
        maxPriority++;
        const selectedStudentElement = document.createElement('li');
        selectedStudentElement.classList.add('student-element');
        selectedStudentElement.textContent = studentSelected.textContent;
        selectedStudentGroup.appendChild(selectedStudentPriority);
        selectedStudentGroup.appendChild(selectedStudentElement);
        right.appendChild(selectedStudentGroup);
        selectedStudentGroup.addEventListener('click', moveLeft);
        left.removeChild(studentSelected);
    }
}

function moveLeft(event) {
    const studentSelected = event.target.closest('.selected-student-group');
    if(event.button === 0) {
        document.querySelectorAll('.selected-student-group').forEach(student => {
            if(student.children[0].textContent > studentSelected.children[0].textContent) {
                student.children[0].textContent--;
            }
        });
        const selectedStudent = document.createElement('li');
        selectedStudent.classList.add('student-element');
        selectedStudent.classList.add('hover-effect');
        selectedStudent.textContent = studentSelected.children[1].textContent;
        left.appendChild(selectedStudent);
        right.removeChild(studentSelected);
        maxPriority--;
        selectedStudent.addEventListener('click', moveRight);
        sortSelectedStudents();
    }
}

function sortSelectedStudents() {
    const selectedStudentGroupsArray = Array.from(document.querySelectorAll('.selected-student-group'));
    const selectedStudentListContainer = document.getElementById('selectedStudents');
    selectedStudentGroupsArray.sort((a, b) => {
        return a.children[0].textContent - b.children[0].textContent;
    }
    );
    selectedStudentListContainer.innerHTML = '';
    selectedStudentGroupsArray.forEach(selectedStudentGroup => {
        selectedStudentListContainer.appendChild(selectedStudentGroup);
    }
    );
}

var searchLeft = document.getElementById('search-left');
var searchRight = document.getElementById('search-right');

searchLeft.addEventListener('keyup', function (event) {
    students = document.querySelectorAll('#students .student-element');
    const searchQuery = event.target.value.toLowerCase();

    for (let i = 0; i < students.length; i++) {
        const currentItem = students[i].textContent.toLowerCase();

        if (currentItem.includes(searchQuery)) {
            students[i].style.display = 'block';
        } else {
            students[i].style.display = 'none';
        }
    }
}
);

searchRight.addEventListener('keyup', function (event) {
    students = document.querySelectorAll('.selected-student-group');
    const searchQuery = event.target.value.toLowerCase();

    for (let i = 0; i < students.length; i++) {
        const currentItem = students[i].children[1].textContent.toLowerCase();

        if (currentItem.includes(searchQuery)) {
            students[i].style.display = 'flex';
        } else {
            students[i].style.display = 'none';
        }
    }
}
);

async function submit() {
    var studentPreferencesLines = document.getElementsByClassName("selected-student-group");
    var studentData = [];

    for (var i = 0; i < studentPreferencesLines.length; i++) {
        var studentName = studentPreferencesLines[i].children[1].innerText;

        studentData.push(studentName);
    }

    var url = "/students";

    try {
        const response = await fetch(url, {
            method: 'POST', 
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(studentData),
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

    var studentArea = document.getElementById("studentArea");
    var successTest = document.createElement("p");
    successTest.innerText = "Students preferences submitted successfully!";
    successTest.style.color = "green";
    studentArea.appendChild(successTest);
    setTimeout(function() {
        studentArea.removeChild(successTest);
    }, 5000);
}