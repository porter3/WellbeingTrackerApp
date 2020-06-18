import React from 'react'
import '../index.css'
import { useSelector } from 'react-redux'

export default function Sidebar() {

    const isActive = useSelector(state => state.sidebar.sidebarIsActive)
    const navClass = isActive ? 'sidebar sidebarActive' : 'sidebar'

    return(
        <nav id="sidebar" className={navClass}>
                <div className="sidebar-header">
                    <h3>menu</h3>
                </div>
                <ul className="list-unstyled components">
                    <li className="active">
                        <a href=""><button className="sidebarLink btn" >view data / log metrics</button></a>
                    </li>
                    <li>
                        <a href="addmetrics"><button className="sidebarLink btn">add metrics</button></a>
                    </li>
                    <li>
                        <a href="removemetrics"><button className="sidebarLink btn">remove metrics</button></a>
                    </li>
                    <li>
                        <a href="usersettings"><button className="sidebarLink btn">user settings</button></a>
                    </li>
                    <li>
                        <a><button className="sidebarLink btn" id="logOutButton" type="submit">log out</button></a>
                    </li>
                </ul>
            </nav>
    )
}