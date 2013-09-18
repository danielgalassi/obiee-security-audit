<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<!--xsl:output method="html"/-->
	<xsl:output method="xml" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN" indent="yes"/>
	<xsl:template match="/">
		<!--html-->
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
		thead th {
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
					<h2>Dashboards &amp; Reports Matrix</h2>
					<br/>
					<!-- Matrix Section -->
					<table>
						<tbody>
							<!-- Dashboard Group / Dashboard Content -->
							<xsl:for-each select="./DashboardGroupList/DashboardGroup">
								<tr>
									<td style="background: #D8D8D8; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
										Dashboard Group: <xsl:value-of select="@DashboardGroupName"/>
									</td>
										<td colspan="5" style="text-align:center; background: #D8D8D8; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555;">
												Role Types
											</td>
									<!--/td-->
								</tr>
								<!-- Dashboard Section -->
								<xsl:for-each select="./Dashboard">
									<tr>
										<!-- Dashboard Names -->
										<td style="background: #E7E7E7; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:20px;">
											Dashboard: <xsl:value-of select="@DashboardName"/>
										</td>
										
										<xsl:choose>
											<xsl:when test="count(.//Permission[@RoleType='Administrators']) > 0">
												<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Administrators</td>
											</xsl:when>
											<xsl:when test="count(.//Permission[@RoleType='Administrators']) = 0">
												<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Administrators</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Permission[@RoleType='Service Desk']) > 0">
												<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Service Desk</td>
											</xsl:when>
											<xsl:when test="count(.//Permission[@RoleType='Service Desk']) = 0">
												<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Service Desk</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Permission[@RoleType='EIM']) > 0">
												<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">EIM</td>
											</xsl:when>
											<xsl:when test="count(.//Permission[@RoleType='EIM']) = 0">
												<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">EIM</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) > 0">
												<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">SuperUsers</td>
											</xsl:when>
											<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) = 0">
												<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">SuperUsers</td>
											</xsl:when>
										</xsl:choose>
										
										<xsl:choose>
											<xsl:when test="count(.//Permission[@RoleType='Business Users']) > 0">
												<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Business Users</td>
											</xsl:when>
											<xsl:when test="count(.//Permission[@RoleType='Business Users']) = 0">
												<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Business Users</td>
											</xsl:when>
										</xsl:choose>
									</tr>
									<xsl:for-each select="./DashboardPageList/DashboardPage">
										<tr>
										<!-- Dashboard Page Names -->
											<td style="background: #F4F4F4; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:40px;">
												Page: <xsl:value-of select="@DashboardPageName"/>
											</td>
											
											<xsl:choose>
												<xsl:when test="count(.//Permission[@RoleType='Administrators']) > 0">
													<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Administrators</td>
												</xsl:when>
												<xsl:when test="count(.//Permission[@RoleType='Administrators']) = 0">
													<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Administrators</td>
												</xsl:when>
											</xsl:choose>
											
											<xsl:choose>
												<xsl:when test="count(.//Permission[@RoleType='Service Desk']) > 0">
													<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Service Desk</td>
												</xsl:when>
												<xsl:when test="count(.//Permission[@RoleType='Service Desk']) = 0">
													<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Service Desk</td>
												</xsl:when>
											</xsl:choose>
											
											<xsl:choose>
												<xsl:when test="count(.//Permission[@RoleType='EIM']) > 0">
													<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">EIM</td>
												</xsl:when>
												<xsl:when test="count(.//Permission[@RoleType='EIM']) = 0">
													<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">EIM</td>
												</xsl:when>
											</xsl:choose>
											
											<xsl:choose>
												<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) > 0">
													<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">SuperUsers</td>
												</xsl:when>
												<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) = 0">
													<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">SuperUsers</td>
												</xsl:when>
											</xsl:choose>
											
											<xsl:choose>
												<xsl:when test="count(.//Permission[@RoleType='Business Users']) > 0">
													<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Business Users</td>
												</xsl:when>
												<xsl:when test="count(.//Permission[@RoleType='Business Users']) = 0">
													<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Business Users</td>
												</xsl:when>
											</xsl:choose>
										</tr>
										<xsl:for-each select="./ReportList/Report">
											<tr>
										<!-- Report Names -->
												<td style="background: #FCFCFC; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #555555; padding-left:60px;">
													Report: <xsl:value-of select="@Name"/>
												</td>
												
												<xsl:choose>
													<xsl:when test="count(.//Permission[@RoleType='Administrators']) > 0">
														<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Administrators</td>
													</xsl:when>
													<xsl:when test="count(.//Permission[@RoleType='Administrators']) = 0">
														<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Administrators</td>
													</xsl:when>
												</xsl:choose>
												
												<xsl:choose>
													<xsl:when test="count(.//Permission[@RoleType='Service Desk']) > 0">
														<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Service Desk</td>
													</xsl:when>
													<xsl:when test="count(.//Permission[@RoleType='Service Desk']) = 0">
														<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Service Desk</td>
													</xsl:when>
												</xsl:choose>
												
												<xsl:choose>
													<xsl:when test="count(.//Permission[@RoleType='EIM']) > 0">
														<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">EIM</td>
													</xsl:when>
													<xsl:when test="count(.//Permission[@RoleType='EIM']) = 0">
														<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">EIM</td>
													</xsl:when>
												</xsl:choose>
												
												<xsl:choose>
													<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) > 0">
														<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">SuperUsers</td>
													</xsl:when>
													<xsl:when test="count(.//Permission[@RoleType='SuperUsers']) = 0">
														<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">SuperUsers</td>
													</xsl:when>
												</xsl:choose>
												
												<xsl:choose>
													<xsl:when test="count(.//Permission[@RoleType='Business Users']) > 0">
														<td width="150px" style="background: #CCFF99; font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: green; text-align:center;">Business Users</td>
													</xsl:when>
													<xsl:when test="count(.//Permission[@RoleType='Business Users']) = 0">
														<td width="150px" style="font-family: Helvetica, sans-serif; font-size: 8pt; font-weight: bold; color: #CCCCCC; text-align:center;">Business Users</td>
													</xsl:when>
												</xsl:choose>
											</tr>
										</xsl:for-each>
									
									</xsl:for-each>
								</xsl:for-each>
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
