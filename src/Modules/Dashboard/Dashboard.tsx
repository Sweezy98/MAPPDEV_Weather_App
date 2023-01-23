import React from 'react';
import { useQuery } from '@apollo/react-hooks';

import { StyledDashboard } from './Dashboard.styled';

interface Props {

}

export const Dashboard: React.FC<Props> = (props: Props) => {
    return (
        <StyledDashboard className="content">
            <h1>Willkommen im Sweather Admin Panel!</h1>
        </StyledDashboard>
    )
}

export default Dashboard
