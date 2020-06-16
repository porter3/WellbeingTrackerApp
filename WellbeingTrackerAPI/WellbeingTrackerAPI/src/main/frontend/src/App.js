import React, { useState } from 'react'
import { Container, Row, Col } from 'react-bootstrap'
import './index.css'
import Sidebar from './components/Sidebar.js'
import SidebarToggle from './components/SidebarToggle.js'
import LoggedInDisplay from './components/LoggedInDisplay.js'
import Graph from './components/Graph.js'

function App() {

  const [ sidebarIsActive, setSidebarIsActive ] = useState(false)

  const handleToggle = () => {
      setSidebarIsActive(!sidebarIsActive)
  }

  return (
    <div className='wrapper'>
      <Sidebar isActive={sidebarIsActive} />
      <Container>
        <Row>
          <Col md={{ span: 6, offset: 4 }}>
            <SidebarToggle handleClick={() => handleToggle()} />
          </Col>
          <Col md={{ span: 6, offset: 6 }}>
            <LoggedInDisplay />
          </Col>
        </Row>
        <Row>
          <Graph />
        </Row>
      </Container>
    </div>
  )
}

export default App;
