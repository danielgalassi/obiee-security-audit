<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" media-type="text/plain"/>
	<xsl:template match="/">
		<WebCatalog>
			<RoleTypeMasterList>
				<RoleType Name="Administrators"/>
				<RoleType Name="Service Desk"/>
				<RoleType Name="EIM"/>
				<RoleType Name="SuperUsers"/>
				<RoleType Name="Business Users"/>
			</RoleTypeMasterList>
			<xsl:for-each select="/WebCat">
				<ApplicationRoleList>
					<xsl:for-each select="./ApplicationRoleList/ApplicationRole">
						<xsl:variable name="myRole" select="@ApplicationRoleName"/>
						<xsl:copy>
							<xsl:copy-of select="@ApplicationRoleName"/>
							<xsl:attribute name="RoleType">
								<xsl:choose>
									<xsl:when test="count(../..//ComponentList/Component[@ComponentName = 'Admin System Privs']/Privilege//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
									<xsl:when test="count(../..//ComponentList/Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
									<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
									<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
									<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
									<xsl:otherwise>Business Users</xsl:otherwise>
								</xsl:choose>
							</xsl:attribute>
						</xsl:copy>
					</xsl:for-each>
				</ApplicationRoleList>
				<xsl:for-each select="./DashboardGroupList">
					<DashboardGroupList>
						<xsl:for-each select="./DashboardGroup">
							<DashboardGroup>
								<xsl:copy-of select="@DashboardGroupName"/>
								<xsl:for-each select="./DashboardList/Dashboard">
									<Dashboard>
										<xsl:copy-of select="@DashboardName"/>
										<PermissionList>
											<xsl:for-each select="./PermissionList/Permission">
												<xsl:variable name="myRole" select="@Role"/>
												<Permission>
													<xsl:copy-of select="@Description"/>
													<xsl:copy-of select="@Role"/>
													<xsl:attribute name="RoleType">
														<xsl:choose>
															<xsl:when test="count(../../../../../..//Component[@ComponentName = 'Admin System Privs']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
															<xsl:when test="count(../../../../../..//Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
															<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
															<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
															<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
															<xsl:otherwise>Business Users</xsl:otherwise>
														</xsl:choose>
													</xsl:attribute>
												</Permission>
											</xsl:for-each>
										</PermissionList>
										<!-- end of dashboard tags section -->
										<DashboardPageList>
											<xsl:for-each select="./DashboardPageList/DashboardPage">
												<DashboardPage>
													<xsl:copy-of select="@DashboardPageName"/>
													<xsl:copy-of select="@isHidden"/>
													<PermissionList>
														<xsl:for-each select="./PermissionList/Permission">
															<xsl:variable name="myRole" select="@Role"/>
															<Permission>
																<xsl:copy-of select="@Description"/>
																<xsl:copy-of select="@Role"/>
																<xsl:attribute name="RoleType">
																	<xsl:choose>
																		<xsl:when test="count(../../../../../../../../..//Component[@ComponentName = 'Admin System Privs']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
																		<xsl:when test="count(../../../../../../../../..//Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
																		<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
																		<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
																		<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
																		<xsl:otherwise>Business Users</xsl:otherwise>
																	</xsl:choose>
																</xsl:attribute>
															</Permission>
														</xsl:for-each>
													</PermissionList>
													<ReportList>
														<xsl:for-each select="./ReportList/Report">
														<Report>
															<xsl:copy-of select="FullUnscrambledName"/>
															<xsl:copy-of select="Name"/>
															<xsl:copy-of select="Owner"/>
													<PermissionList>
														<xsl:for-each select="./PermissionList/Permission">
															<xsl:variable name="myRole" select="@Role"/>
															<Permission>
																<xsl:copy-of select="@Description"/>
																<xsl:copy-of select="@Role"/>
																<xsl:attribute name="RoleType">
																	<xsl:choose>
																		<xsl:when test="count(../../../../../../../../../../..//Component[@ComponentName = 'Admin System Privs']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">Administrators</xsl:when>
																		<xsl:when test="count(../../../../../../../../../../..//Component[@ComponentName = 'generalprivs']/Privilege[@PrivilegeName='Global Answers']//Role[@access='Granted'][.=$myRole and not(contains($myRole, 'Service Desk') or contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read') or contains($myRole, 'BISystem') or contains($myRole, 'AuthenticatedUser'))]) > 0">SuperUsers</xsl:when>
																		<xsl:when test="contains($myRole, 'NBN EIM') or contains($myRole, 'Session_Log_Read')">EIM</xsl:when>
																		<xsl:when test="contains($myRole, 'Service Desk')">Service Desk</xsl:when>
																		<xsl:when test="contains($myRole, 'BISystem') or  contains($myRole, 'AuthenticatedUser')">System User</xsl:when>
																		<xsl:otherwise>Business Users</xsl:otherwise>
																	</xsl:choose>
																</xsl:attribute>
															</Permission>
														</xsl:for-each>
													</PermissionList>
														</Report>
														</xsl:for-each>
													</ReportList>
												</DashboardPage>
											</xsl:for-each>
										</DashboardPageList>
									</Dashboard>
								</xsl:for-each>
							</DashboardGroup>
						</xsl:for-each>
					</DashboardGroupList>
				</xsl:for-each>
			</xsl:for-each>
		</WebCatalog>
	</xsl:template>
</xsl:stylesheet>
