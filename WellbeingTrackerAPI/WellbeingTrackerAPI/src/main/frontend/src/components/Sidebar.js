import React, { useState } from 'react'
import { Nav } from 'react-bootstrap'
import { List, ListItem, ListItemText } from '@material-ui/core'
import '../index.css'

export default function Sidebar({ isActive }) {

    const className = isActive ? 'sidebar sidebarActive' : 'sidebar'

    return(
        <Nav>
            <div className={className}>
                <List>
                    <ListItem button>
                        <ListItemText>home</ListItemText>
                    </ListItem>
                    <ListItem button>
                        <ListItemText>log out</ListItemText>
                    </ListItem>
                </List>
            </div>
        </Nav>
    )
}