import React, { useState } from 'react'
import { Provider } from 'react-redux'
import store from './redux/store'
import './index.css'
import { Container, Row, Col } from 'reactstrap'
import Sidebar from './components/Sidebar.js'
import SidebarToggle from './components/SidebarToggle.js'
import LoggedInDisplay from './components/LoggedInDisplay.js'
import Graph from './components/Graph.js'

export default function App() {

  const [ sidebarIsActive, setSidebarIsActive ] = useState(false)

  const handleToggle = () => {
      setSidebarIsActive(!sidebarIsActive)
  }

  return (
    <Provider store={store}>
      <div className='wrapper'>
        <Sidebar isActive={sidebarIsActive} />
          <div style={{ paddingLeft: '40px' }}>
            <Container>
              <Row>
                <Col md='6'>
                  <SidebarToggle handleClick={() => handleToggle()} />
                </Col>
                <Col md='4'>
                  <LoggedInDisplay />
                </Col>
              </Row>
              <Row>
                <Graph />
              </Row>
            </Container>
          </div>
      </div>
    </Provider>
  )
}