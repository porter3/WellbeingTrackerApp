import {
    FETCH_ENTRIES_REQUEST,
    FETCH_ENTRIES_SUCCESS,
    FETCH_ENTRIES_FAILURE
} from './appTypes'

export const fetchEntriesRequest = () => {
    return {
        type: FETCH_ENTRIES_REQUEST
    }
}

export const fetchEntriesSuccess = entries => {
    return {
        type: FETCH_ENTRIES_SUCCESS,
        payload: entries
    }
}

export const fetchEntriesFailure = error => {
    return {
        type: FETCH_ENTRIES_FAILURE,
        payload: error
    }
}