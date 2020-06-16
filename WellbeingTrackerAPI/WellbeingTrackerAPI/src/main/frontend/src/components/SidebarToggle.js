import React from 'react'
import MenuIcon from '@material-ui/icons/Menu'

export default function SidebarToggle({ handleClick }) {
    return(
        <div><MenuIcon onClick={handleClick} /></div>
    )
}