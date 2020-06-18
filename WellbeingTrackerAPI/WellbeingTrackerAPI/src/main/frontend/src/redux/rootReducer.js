import { combineReducers } from 'redux'
import sidebarReducer from './sidebar/sidebarReducer'
// import all necessary component reducers

const rootReducer = combineReducers({
    sidebar: sidebarReducer
})

export default rootReducer