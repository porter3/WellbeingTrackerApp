import TOGGLE_ACTIVE_STATUS from './sidebarTypes'

const initialState = {
    isActive: false 
}

const sidebarReducer = (state = initialState, action) => {
    switch(action.type) {
        case TOGGLE_ACTIVE_STATUS:
            return {
                ...state,
                isActive: !state.isActive
            }
        default:
            return state
    }
}

export default sidebarReducer