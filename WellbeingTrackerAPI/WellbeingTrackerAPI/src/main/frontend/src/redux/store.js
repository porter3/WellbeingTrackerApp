import { createStore, applyMiddleware } from 'redux'
import { composeWithDevTools } from 'redux-devtools-extension'
// thunk
// logger
import rootReducer from './rootReducer'

const store = createStore(rootReducer, composeWithDevTools())

export default store