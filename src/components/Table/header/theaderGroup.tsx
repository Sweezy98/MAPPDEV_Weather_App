import React from 'react'

import { StyledTHeaderGroup } from './theaderGroup.styled';

type Props = {
    children: React.ReactNode,
    columnGrid: string,
}

const THeaderGroup: React.FC<Props> = (props: Props) => {
  return (
    <StyledTHeaderGroup columnGrid={props.columnGrid}>
        {props.children}
    </StyledTHeaderGroup>
  )
}

export default THeaderGroup