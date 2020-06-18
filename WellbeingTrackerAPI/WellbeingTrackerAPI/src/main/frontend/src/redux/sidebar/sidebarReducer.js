import TOGGLE_ACTIVE_STATUS from './sidebarTypes'

const initialState = {
    sidebarIsActive: false 
}

const sidebarReducer = (state = initialState, action) => {
    switch(action.type) {
        case TOGGLE_ACTIVE_STATUS:
            return {
                ...state,
                sidebarIsActive: !state.sidebarIsActive
            }
        default:
            return state
    }
}

export default sidebarReducer