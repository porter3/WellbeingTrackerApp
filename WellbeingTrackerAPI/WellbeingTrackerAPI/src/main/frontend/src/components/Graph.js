import React, { useState, useEffect } from 'react'
import axios from 'axios'

export default function Graph() {

    const [ variable, setVariable ] = useState({})

    useEffect(() => {
        axios.get('http://localhost:8080/api/test')
        .then(response => {
            console.log(response)
            setVariable(response)
        })
        .catch(response => {
            console.log(response)
        })
    }, [])

    return(
        <p>hello</p>
    )
}