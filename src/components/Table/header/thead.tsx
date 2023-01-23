import React from 'react'
import { SortDirection } from '@tanstack/react-table'

import { MdKeyboardArrowDown, MdKeyboardArrowUp } from 'react-icons/md';

import { StyledTHead } from "./thead.styled";

interface Props {
    children: React.ReactNode,
    isSortable: boolean,
    isSorted: false | SortDirection,
    toggleSort: () => void,
}

const THead: React.FC<Props> = (props: Props) => {
  return (
    <StyledTHead onClick={() => props.toggleSort()} isSortable={props.isSortable}>
        <p>{props.children}</p>
        {
            props.isSortable
            ? props.isSorted === 'asc'
              ? <MdKeyboardArrowUp/>
              : props.isSorted === 'desc'
                ? <MdKeyboardArrowDown/>
                : null
            : null
        }
    </StyledTHead>
  )
}

export default THead