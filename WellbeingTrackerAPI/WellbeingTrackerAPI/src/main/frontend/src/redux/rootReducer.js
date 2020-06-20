import { combineReducers } from 'redux'
import appReducer from './app/appReducer'
import sidebarReducer from './sidebar/sidebarReducer'

const rootReducer = combineReducers({
    app: appReducer,
    sidebar: sidebarReducer
})

export default rootReducer