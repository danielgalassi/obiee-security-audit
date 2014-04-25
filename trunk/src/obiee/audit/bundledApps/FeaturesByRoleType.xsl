<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes"/>
	<xsl:template match="/">
		<html xmlns="http://www.w3.org/1999/xhtml">
			<head>
				<style type="text/css">
		h1 {font-family: Helvetica, sans-serif; font-weight: bold; font-size: 24pt; color: #676767;}
		h2 {font-family: Helvetica, sans-serif; font-weight: bold; font-size: 20pt; color: #777777;}
		h3 {font-family: Helvetica, sans-serif; font-size: 14pt; color: #777777;}
		h4 {font-family: Helvetica, sans-serif; font-size: 14pt; color: #888888;}
		li {font-family: Helvetica, sans-serif; font-size: 10pt; color: #474747;}
		a  {font-family: Helvetica, sans-serif; font-size: 10pt; color: #444444;}
		*  {font-family: Helvetica, sans-serif; font-size: 8pt; color: #333333;}
		tr {height:30; font-size: 8.5pt;}
		tr:hover {background: rgb(248,248,248);}
		table {
			border-spacing: 0 0;
			margin: 1px;
			border-right: 1px solid #DEDEDE;
			font-family: Helvetica, sans-serif; font-size: 8.5pt;}
		th {
			font-family: Helvetica, sans-serif; font-size: 8pt;
			background: #EFEFEF;
			border-left: 1px solid #DEDEDE;
			border-top: 1px solid #DEDEDE;}
		tbody td {
        		font-family: Helvetica, sans-serif; font-size: 8.5pt;
			border-bottom: 1px solid #DEDEDE;
			border-left: 1px solid #DEDEDE;}
				</style>
				
				<title>Oracle Business Intelligence Security Matrix - Features</title>
			</head>
			<body>
				<!-- Security Matrix heading -->
				<h1>OBIEE Security Setup</h1>
				<br/>
				<xsl:for-each select="/WebCatalog">
					<h2>Feature Privilege Matrix</h2>
					<br/>
					<!-- Matrix Section -->
					<table>
						<tbody>
							<tr>
								<td style="text-align:center; font-family: Helvetica, sans-serif; font-size: 8.5pt; border-top: 1px solid #EFEFEF; border-left: 1px solid #EFEFEF; border-bottom: 1px solid #E3E3E3; background-color: rgb(250,250,250);">Functionality / Role Types</td>
								<th colspan="5" style="background: #DFDFDF; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #333333; border-right: 0px solid; border-left: 0px solid;">Role Types</th>
							</tr>
							<!-- Component / Privilege Content -->
							<xsl:for-each select="./ComponentList/Component[not (starts-with(@ComponentName, 'SA.')) and not (starts-with(@ComponentName, 'View.'))]">
								<tr>
									<td style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
										<xsl:choose>
											<xsl:when test="starts-with(@ComponentName, 'View.') and (substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'view' or substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'View')">
												<xsl:value-of select="substring(@ComponentName, 6, (string-length(@ComponentName)-9))"/> view</xsl:when>
											<xsl:when test="starts-with(@ComponentName, 'Search System Privs')">Answers (Self-Service)</xsl:when>
											<xsl:when test="@ComponentName = 'security'">Security Management</xsl:when>
											<xsl:when test="@ComponentName = 'catalog'">Presentation Catalogue Management</xsl:when>
											<xsl:when test="@ComponentName = 'catalogsystemprivs'">Presentation Catalogue Management (II)</xsl:when>
											<xsl:when test="@ComponentName = 'generalprivs'">General Application Management</xsl:when>
											<xsl:when test="@ComponentName = 'Admin System Privs'">System Management</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="@ComponentName"/>
											</xsl:otherwise>
										</xsl:choose>
									
									</td>

									<!-- greyed out cells -->
									<xsl:for-each select="../../..//RoleType">
										<td width="150px" style="text-align:center; background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
											<xsl:value-of select="@Name"/>
										</td>
									</xsl:for-each>
								</tr>
								<!-- Privilege Section -->
								<xsl:for-each select="./Privilege">
									<tr>
										<!-- Privilege Names -->
										<td style="background: #F6F6F6; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:20px;">
											<xsl:value-of select="@PrivilegeName"/>
										</td>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">YES</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">No</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">YES</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">No</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">YES</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">No</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">YES</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">No</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">YES</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">No</td>
											</xsl:when>
										</xsl:choose>
									</tr>
								</xsl:for-each>
							</xsl:for-each>
							<tr>
								<td style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">Self-Service Subject Areas</td>
								<td colspan="5" style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center; border-right: 0px solid; border-left: 0px solid;"/>
							</tr>
							<xsl:for-each select="./ComponentList/Component[starts-with(@ComponentName, 'SA.')]">
								<tr>
									<td style="background: #F6F6F6; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:20px;">
										Subject Area <xsl:value-of select="substring(@ComponentName,4,string-length(@ComponentName)-3)"/>
									</td>
									<!-- Privilege Section -->
									<xsl:for-each select="./Privilege">
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Administrators</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Service Desk</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">EIM</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">SuperUsers</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Business Users</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
									</xsl:for-each>
								</tr>
							</xsl:for-each>
							<tr>
								<td style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">Answers Views</td>
								<td colspan="5" style="background: #ECECEC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center; border-right: 0px solid; border-left: 0px solid;"/>
							</tr>
							<xsl:for-each select="./ComponentList/Component[starts-with(@ComponentName, 'View.')]">
								<tr>
									<td style="background: #F6F6F6; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:20px;">
										Edit or Customise 
										<xsl:choose>
											<xsl:when test="(substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'view' or substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'View')">
												<xsl:value-of select="normalize-space(substring(@ComponentName, 6, (string-length(@ComponentName)-9)))"/> view
											</xsl:when>
											<xsl:when test="substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'ompt' or substring(@ComponentName, (string-length(@ComponentName)-3), 4) = 'ctor'">
												<xsl:value-of select="normalize-space(substring(@ComponentName, 6, (string-length(@ComponentName)-5)))"/>
											</xsl:when>
											<xsl:otherwise>
												<xsl:value-of select="@ComponentName"/>
											</xsl:otherwise>
										</xsl:choose>
									</td>
									<!-- Privilege Section -->
									<xsl:for-each select="./Privilege">
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Administrators</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Administrators']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Service Desk</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Service Desk']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">EIM</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='EIM']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">SuperUsers</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='SuperUsers']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) > 0">
												<td style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Business Users</td>
											</xsl:when>
											<xsl:when test="count(.//Role[@access='Granted' and @RoleType='Business Users']) = 0">
												<td style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #444444; text-align:center;">-</td>
											</xsl:when>
										</xsl:choose>
									</xsl:for-each>
								</tr>
							</xsl:for-each>
						</tbody>
					</table>
					
					<br/>
					<br/>
					<hr style="height: 1px; border: 0; background-color: #AAAAAA; width: 70%;"/>
				
				</xsl:for-each>
				<div style="padding-top: 20px;">
					<p style="color: rgb(128, 128, 128); float: left;">Mozilla Firefox or Google Chrome are strongly recommended for best results.</p>
					<p style="color: rgb(128, 128, 128); float: right;">Generated using <a href="http://code.google.com/p/obiee-security-audit/" style="font-family: Helvetica, sans-serif; font-size: 8pt;color: rgb(128, 128, 128);" target="_blank">OBIEE Security Audit</a> application.</p>
				</div>
				<p style="color: rgb(0, 0, 255); float: center;"> This HTML page is W3C compliant.</p>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>
