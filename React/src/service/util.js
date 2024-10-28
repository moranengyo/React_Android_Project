function commaNumber(number) {
    return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

function dateFormat(date){
    let dateObj = new Date(date);
return date;
   // return `${dateObj.getFullYear()} ${dateObj.getMonth()} ${dateObj.getUTCDay()}`
}


export {commaNumber, dateFormat}