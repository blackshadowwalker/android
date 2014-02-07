<%@ page language="java" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>导航菜单</title>

</head>
<body>

   <!-- NAVBAR
    ================================================== -->
    <div class="navbar-wrapper">
      <!-- Wrap the .navbar in .container to center it within the absolutely positioned parent. -->
      <div class="container">
        <div class="navbar navbar-inverse" >
          <div class="navbar-inner">
            <!-- Responsive Navbar Part 1: Button for triggering responsive navbar (not covered in tutorial). Include responsive CSS to utilize. -->
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
              <span class="icon-bar"></span>
            </button>
            <a class="brand" href="home">Black Shadow</a>
            <!-- Responsive Navbar Part 2: Place all navbar contents you want collapsed withing .navbar-collapse.collapse. -->
            <div class="nav-collapse collapse">
              <ul class="nav">
                <li class="active"><a href="home">Home</a></li>
                <li><a href="#about">Announcements</a></li>
                <li><a href="wiki.jsp">Wiki</a></li>
                <li><a href="download">Download</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#contact">Contact Us</a></li>
                <li><a href="navigation.jsp">More</a></li>
                <!-- Read about Bootstrap dropdowns at http://twitter.github.com/bootstrap/javascript.html#dropdowns -->
              </ul>
              <ul class="nav pull-right">
              	<li>
              		<div class="btn-group" id=userpanel>
					  <a class="btn btn-primary" href="#"><i class="icon-user icon-white"></i> ${user.username}</a>
					  <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#"><span class="caret"></span></a>
					  <ul class="dropdown-menu">
					    <li><a href="#"><i class="icon-pencil"></i> Edit</a></li>
					    <li><a href="#"><i class="icon-trash"></i> Delete</a></li>
					    <li><a href="#"><i class="icon-ban-circle"></i> Ban</a></li>
					    <li class="divider"></li>
					    <li><a href="#"><i class="i"></i> Make admin</a></li>
					  </ul>
					</div>
              	</li>
                <li class="dropdown ">
                  <a href="#" class="dropdown-toggle" data-toggle="dropdown">Account <b class="caret"></b></a>
                  <ul class="dropdown-menu">
                    <li><a href="#login" data-toggle="modal">Login</a></li>
                    <li><a href="login.jsp" data-toggle="modal" data-target="#login" >Register</a></li>
                    <li><a href="user"  >User</a></li>
                    <li><a href="apk"  >apk</a></li>
                    <li><a href="modules/user/list.jsp"  >List User</a></li>
                    <li><a href="online.jsp"  >Online User</a></li>
                    <li class="divider"></li>
                    <li><a href="chart/xfchart"  >消防统计分析</a></li>
                    <li><a href="chart/clchart"  >卡口统计分析</a></li>
                    <li><a href="Service"  >Service</a></li>
                    <li><a href="#">Forgot Password?</a></li>
                  </ul>
                </li>
	           </ul>
            </div><!--/.nav-collapse -->
          </div><!-- /.navbar-inner -->
        </div><!-- /.navbar -->

      </div> <!-- /.container -->
    </div><!-- /.navbar-wrapper -->

</body>
</html>