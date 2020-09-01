import React from 'react'
import MenuIcon from '@material-ui/icons/Menu'
import { useDispatch } from 'react-redux'
import { toggleActiveStatus } from '../redux'

export default function SidebarToggle({ handleClick }) {

    const dispatch = useDispatch()
    
    return(
        <div>
            <MenuIcon onClick={() => dispatch(toggleActiveStatus())} />
        </div>
    )
}