const countDownElem = document.getElementById("timeLeft");
let time = countDownElem ? parseInt(countDownElem.innerText) : 0;
let timerInterval;
function updateCountDown()
{
    if (!countDownElem || time <= 0) {
        clearInterval(timerInterval);
        return;
    }
    let days = Math.floor(time / (24 * 60 * 60));
    let hours = Math.floor((time % (24 * 60 * 60)) / (60 * 60));
    let minutes = Math.floor((time % (60 * 60)) / 60);
    let seconds = Math.floor(time % 60);

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;

    if (days > 0) {
        days = days < 10 ? '0' + days : days;
        countDownElem.innerText = `${days}:${hours}:${minutes}:${seconds}`;
    } else if (hours > 0) {
        countDownElem.innerText = `${hours}:${minutes}:${seconds}`;
    } else {
        countDownElem.innerText = `${minutes}:${seconds}`;
    }

    time--;
}

timerInterval = setInterval(updateCountDown, 1000);