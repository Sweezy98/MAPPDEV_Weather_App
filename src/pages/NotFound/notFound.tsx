import React from 'react';
import { useLocation } from 'react-router-dom';

import { StyledNotFoundPage } from './notFound.styled';

interface Props {

}


export const NotFoundPage: React.FC<Props> = () => {
    const {pathname} = useLocation();
    return (
        <StyledNotFoundPage className="content">
            <h1>Dieses Feature existiert nicht!</h1>
            <p>Das Feature "{pathname}" wurde nicht gefunden</p>
        </StyledNotFoundPage>
    )
}

export default NotFoundPage
