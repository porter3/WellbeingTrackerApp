import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import axios from 'axios'
import './index.css'
import { Container, Row, Col } from 'reactstrap'
import Sidebar from './components/Sidebar.js'
import SidebarToggle from './components/SidebarToggle.js'
import LoggedInDisplay from './components/LoggedInDisplay.js'
import Graph from './components/Graph.js'
import { fetchEntriesRequest } from './redux/app/appActions'

export default function App() {

  const dispatch = useDispatch()

    useEffect(() => {
        async function fetchData() {
            axios.get('http://localhost:8080/api/metrictypes/3')
            .then(response => {
                console.log(response.data)
                dispatch(fetchEntriesRequest())
            })
            .catch(response => {
                console.log(response)
            })
        }
        fetchData()
    }, [dispatch])

  return (
    <div className='wrapper'>
      <Sidebar />
        <div style={{ paddingLeft: '40px' }}>
          <Container>
            <Row>
              <Col md='6'>
                <SidebarToggle />
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
  )
}