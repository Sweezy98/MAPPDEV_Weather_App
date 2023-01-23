import React from 'react'
import { HiOutlineCommandLine } from 'react-icons/hi2';
import { AiOutlineDashboard, AiOutlineUser } from 'react-icons/ai';
import { MdHttp } from 'react-icons/md';
import { IoSettingsOutline } from 'react-icons/io5';

import TopNavItem from './item/topNavItem';

import { ReactComponent as Logo } from '../../../assets/Logo.svg';

import { StyledTopNav } from './topNav.styled';

interface Props {
    
}

export const TopNav: React.FC<Props> = (props: Props) => {
    return (
        <StyledTopNav>
            <div className="app_logo">
                <Logo />
            </div>
            
            <TopNavItem icon={<AiOutlineDashboard />} text={"Dashboard"} link={'/dashboard'} />
            <TopNavItem icon={<AiOutlineUser />} text={"Users"} link={'/users'} />
            <TopNavItem icon={<MdHttp />} text={"Sessions"} link={'/sessions'} />
            <TopNavItem icon={<HiOutlineCommandLine />} text={"Logs"} link={'/logs'} />
            <TopNavItem icon={<IoSettingsOutline />} text={"Settings"} link={'/settings'} />
        </StyledTopNav>
    )
}

export default TopNav