import {
    FETCH_ENTRIES_REQUEST,
    FETCH_ENTRIES_SUCCESS,
    FETCH_ENTRIES_FAILURE
} from './appTypes'

const initialState = {
    loading: false,
    entries: [],
    error: ""
}

const reducer = (state = initialState, action) => {
    switch(action.type) {
        case FETCH_ENTRIES_REQUEST:
            return {
                ...state,
                loading: true
            }
        case FETCH_ENTRIES_SUCCESS:
            return {
                ...state,
                loading: false,
                entries: action.payload
            }
        case FETCH_ENTRIES_FAILURE:
            return {
                ...state,
                loading: false,
                error: action.payload
            }
        default:
            return state
    }
}

export default reducer