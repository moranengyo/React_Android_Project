import {format, parseISO} from "date-fns";

export const colors = {
        primary: '#0F4C81',
        primaryBack: '#2c6192',
        secondary: '#41608b',
        secondaryBack: '#e6effa',
        red: '#c64936',
        redBack: '#fde8e5',
        wholeBack: '#f2f8ff',
        request: '#4978fd',
        approve: '#29a95d',
        reject: '#fd6c6d',
        lightGrey: '#d8d8d8',
};

export const parseImageName = (imageUrl) => {
        if (imageUrl == null) {
                return ""
        }
        return imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
}

export const formatDateString = (dateString) => {
        const date = parseISO(dateString);
        return format(date, 'yyyy-MM-dd  HH:mm:ss');
}

export const role = true;
